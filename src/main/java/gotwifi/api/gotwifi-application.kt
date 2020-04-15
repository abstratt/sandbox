package gotwifi.api

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.apache.catalina.filters.RequestDumperFilter
import org.springframework.boot.web.servlet.FilterRegistrationBean



@Configuration
@EnableJpaRepositories(basePackages = arrayOf("gotwifi/api"))
@EntityScan(basePackages = arrayOf("gotwifi/api"))
@EnableTransactionManagement
open class GotWifiConfig

@SpringBootApplication
open class Application

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}
