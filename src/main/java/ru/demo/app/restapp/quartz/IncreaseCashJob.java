package ru.demo.app.restapp.quartz;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;
import ru.demo.app.restapp.service.QuartzJobService;

@Slf4j
@Component
@RequiredArgsConstructor
public class IncreaseCashJob implements Job {

  private final QuartzJobService service;

  @Override
  public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    log.debug("Starting increase cash by Job");
    service.increaseCashOfAllProfiles();
    log.debug("Finish increase cash by Job");
  }
}
