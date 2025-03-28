package com.example.nurim.domain.application.service;

import com.example.nurim.domain.application.repository.ApplicationRepository;
import com.example.nurim.domain.program.entity.Category;
import com.example.nurim.domain.program.entity.Program;
import com.example.nurim.domain.program.entity.ProgramDate;
import com.example.nurim.domain.program.enums.ProgramStatus;
import com.example.nurim.domain.program.repository.CategoryRepository;
import com.example.nurim.domain.program.repository.ProgramDateRepository;
import com.example.nurim.domain.program.repository.ProgramRepository;
import com.example.nurim.domain.user.entity.User;
import com.example.nurim.domain.user.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ApplicationConcurrencyControlTest {

    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProgramRepository programRepository;
    @Autowired
    private ProgramDateRepository programDateRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ApplicationRepository applicationRepository;

    private Long programDateId;
    private Program program;
    private List<User> users;

    @BeforeAll
     void beforeAll() {
        Category category = new Category("category1");
        categoryRepository.save(category);

        LocalDateTime now = LocalDateTime.now();
        program = new Program(category, "title", "location", 100L, "detail", ProgramStatus.ACCEPTING, now.minusDays(3), now.plusDays(3), "phone");
        programRepository.save(program);

        users = IntStream.rangeClosed(1, 1000)
                .mapToObj(i -> new User("email" + i, "password", "name" + i))
                .toList();
        userRepository.saveAll(users);
    }

    @BeforeEach
    void beforeEach() {
        ProgramDate programDate = new ProgramDate(program, LocalDateTime.now().plusMonths(1));
        programDateRepository.save(programDate);
        programDateId = programDate.getId();
    }

    @AfterEach
    void afterEach() {
        applicationRepository.deleteAll();
        programDateRepository.deleteAll();
    }

    @AfterAll
    void tearDown() {
        userRepository.deleteAll();
        programRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    @Order(1)
    void 프로그램_순차적_신청_성공() {
        Long quota = program.getQuota();

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        for (User user : users) {
            try {
                applicationService.createApplication(user.getId(), programDateId);
                successCount.incrementAndGet();
            } catch (Exception e) {
                failCount.incrementAndGet();
            }
        }

        System.out.println("quota: " + quota);
        System.out.println("successCount: " + successCount.get());
        System.out.println("failCount: " + failCount.get());
    }

    @Test
    @Order(2)
    void 프로그램_동시_신청_동시성_문제_발생() throws InterruptedException {
        Long quota = program.getQuota();

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch countDownLatch = new CountDownLatch(users.size());

        for (User user : users) {
            executorService.submit(() -> {
                try {
                    applicationService.createApplication(user.getId(), programDateId);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failCount.incrementAndGet();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();

        System.out.println("quota: " + quota);
        System.out.println("successCount: " + successCount.get());
        System.out.println("failCount: " + failCount.get());
    }
}