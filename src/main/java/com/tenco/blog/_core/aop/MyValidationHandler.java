package com.tenco.blog._core.aop;

import com.tenco.blog._core.errors.exception.Exception400;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

/**
 * AOP를 활용한 유효성 검사 자동화 처리
 * POST, PUT 요청에서 발생하는 유효성 검사 유무를 자동으로 처리
 */

@Aspect
@Component
@Slf4j
public class MyValidationHandler {

    /**
     * 동작 원리
     * 1. POST, PUT 요청이 Controller 메서드에 도달하기 전에 가로채기
     * 2. 메서드의 매개 변수 중 Errors 객체 찾기
     * 2. 유효성 검사 오류가 있으면 Exception400 던지기
     */

    @Before("@annotation(org.springframework.web.bind.annotation.PutMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void validationCheck(JoinPoint joinPoint) {
        log.debug("=== AOP 유효성 검사 시작 ===");
        log.debug("실행 메서드 - {}", joinPoint.getSignature().getName());

        // joinPoint 에서 메서드의 모든 매개변수 가져오기
        Object[] args = joinPoint.getArgs();

        // 매개변수를 하나씩 검사해서 Errors 객체를 찾기
        for (Object arg : args) {
            // instanceof : 객체가 특정 타입인지 확인하는 연산자
            if (arg instanceof Errors) {
                log.debug("Errors 객체 발견 - 유효성 검사 진행");
                Errors errors = (Errors) arg;
                // 오류가 있다면
                if (errors.hasErrors()) {
                    log.warn("유효성 검사 오류 발견 - 오류 개수 : {}", errors.getErrorCount());
                    FieldError firstError = errors.getFieldErrors().get(0);
                    String errorMessage = firstError.getDefaultMessage() + " : " + firstError.getField();
                    throw new Exception400(errorMessage);
                }
                log.debug("유효성 검사 통과");
                break;
            }
        }
        log.debug("=== AOP 유효성 검사 완료 ===");
    }
}
