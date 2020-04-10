package com.abstratt.kotlin_aop

import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around

@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION)
annotation class Debuggable

@Aspect
public class DebuggingAspect {
    @Around("@annotation(Debuggable) && execution(* *(..))")
    @Throws(Throwable::class)
    fun around(pjp: ProceedingJoinPoint): Any? {
        println("Before " + pjp.signature.name)
        println("Args " + pjp.args.toList())
        println("Target " + pjp.target)
        val result = pjp.proceed()
        println("After " + pjp.signature.name)
        println("Target " + pjp.target)
        return result
    }
}