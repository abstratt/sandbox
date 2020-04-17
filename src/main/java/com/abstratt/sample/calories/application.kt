package com.abstratt.sample.calories

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.security.authentication.AuthenticationDetailsSource
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.transaction.annotation.EnableTransactionManagement
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2
import java.time.LocalTime
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter


@Configuration
@EnableJpaRepositories(basePackages = ["com.abstratt.sample"])
@EntityScan(basePackages = ["com.abstratt.sample"])
@EnableTransactionManagement
@EnableScheduling
open class CaloriesAppConfig


@Configuration
open class APIConfiguration {
    @Value("\${cors.allowed.origins}")
    lateinit var corsAllowedOrigins : String

    @Bean
    fun  webMvcConfigurer() : WebMvcConfigurer {
        /**
         * @see http://blog.netgloo.com/2015/05/19/spring-boot-avoid-pathvariable-parameters-getting-truncated-on-dots/
         */
        return object : WebMvcConfigurerAdapter() {
        	override fun configurePathMatch(configurer : PathMatchConfigurer) {
                configurer.isUseSuffixPatternMatch = false
        	}
        }
    }

	@Bean
	fun corsFilterRegistration() : FilterRegistrationBean {
		val source = UrlBasedCorsConfigurationSource()
		val config = CorsConfiguration()
		config.setAllowCredentials(true);
		config.setAllowedOrigins(corsAllowedOrigins.split(","))
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", config)
		val bean = FilterRegistrationBean(CorsFilter(source))
		bean.setOrder(0)
		return bean;
	}
}

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
open class CaloriesSecurityConfig : WebSecurityConfigurerAdapter() {

    @Autowired
    lateinit var userDetailsService : CustomUserDetailsService

    @Throws(Exception::class)
    protected open override fun configure(http: HttpSecurity) {
        http
                .anonymous().authorities(SecurityRole.Anonymous.authority).and()
                .csrf().disable()
                .userDetailsService(userDetailsService)
                .httpBasic()
    }

}

@Configuration
@EnableSwagger2
@Profile("production", "development")
open class CaloriesApiDocConfig {
    @Bean
    open fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .directModelSubstitute(LocalTime::class.java, String::class.java)
                .select()
                .apis(RequestHandlerSelectors.basePackage(javaClass.`package`.name))
                .paths(PathSelectors.any())
                .build()
    }
}

@SpringBootApplication
open class CaloriesApp

fun main(args: Array<String>) {
    SpringApplication.run(CaloriesApp::class.java, *args)
}