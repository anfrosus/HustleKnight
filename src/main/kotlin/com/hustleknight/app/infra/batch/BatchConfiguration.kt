package com.hustleknight.app.infra.batch

import com.hustleknight.app.model.Player
import com.hustleknight.app.repository.PlayerRepository
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.SimpleAsyncTaskExecutor
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class BatchConfiguration(
    private val playerRepository: PlayerRepository,
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
    private val reader: PlayerItemReader,
    private val writer: PlayerItemWriter,
    private val processor: PlayerItemProcessor

) {

    @Bean
    fun settleScoreJob(jobRepository: JobRepository): Job {
        return JobBuilder("settleScoreJob", jobRepository)
            .incrementer(RunIdIncrementer())
            .start(settleScoreStep(jobRepository, transactionManager))
            .build()
    }

    @Bean
    fun settleScoreStep(jobRepository: JobRepository, transactionManager: PlatformTransactionManager): Step {
        return StepBuilder("settleScoreStep", jobRepository)
            .allowStartIfComplete(true)
            .chunk<Player, Player>(1000, transactionManager)
            .reader(reader)
            .processor(processor)
            .writer(writer)
            .build()
    }

    @Bean
    fun settleScoreJobLauncher(): JobLauncher {
        val jobLauncher = TaskExecutorJobLauncher()
        jobLauncher.setJobRepository(jobRepository)
        jobLauncher.setTaskExecutor(SimpleAsyncTaskExecutor())
        jobLauncher.afterPropertiesSet()
        return jobLauncher
    }

    @Scheduled(cron = "0 0 0 * * ?")
    fun launchJob() {
        val jobLauncher = settleScoreJobLauncher()
        jobLauncher.run(settleScoreJob(jobRepository), JobParameters())
    }
}