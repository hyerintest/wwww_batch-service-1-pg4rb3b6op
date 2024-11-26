package com.tlc.test.job;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
public class SimpleBatchJob {

    @Bean
    public Step myStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("step1", jobRepository)
            .<String, String>chunk(2, transactionManager)
            .reader(new ListItemReader<>(Arrays.asList("item1", "item2", "item3", "item4", "item5", "item6")))
            .processor(item -> {
                log.info("SimpleBatchJob: process {}", item);
                return item;
            })
            .writer(chunk -> log.info("SimpleBatchJob: writer {}", chunk))
            .build();
    }

    @Bean
    public Job simpleJob(JobRepository jobRepository, Step step1) {
        return new JobBuilder("simpleJob", jobRepository)
            .start(step1)
            .build();
    }

}