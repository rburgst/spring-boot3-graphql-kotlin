package com.example.demo

import mu.KotlinLogging
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

private val logger = KotlinLogging.logger {}

@SpringBootApplication
class DemoApplication {
    @Bean
    fun dataIngester(clubRepository: ClubRepository, athleteRepository: AthleteRepository): ApplicationRunner =
        ApplicationRunner {
            logger.debug { "DemoApplication.dataIngester started" }
            val club1 = clubRepository.save(Club("club1"))
            val ath1 = athleteRepository.save(Athlete(club1, "Brown", "Allen", Gender.MALE))
            val ath2 = athleteRepository.save(Athlete(club1, "Doe", "John", Gender.MALE))
            val club2 = clubRepository.save(Club("club2"))
            val ath3 = athleteRepository.save(Athlete(club2, "Osinski", "Joann", Gender.FEMALE))
            val ath4 = athleteRepository.save(Athlete(club2, "Doe", "Jane", Gender.FEMALE))
            logger.debug { "DemoApplication.dataIngester done" }
        }

    @Bean
    fun securityFilterChain(http: HttpSecurity) =
        http.authorizeHttpRequests {
            it.anyRequest().permitAll()
        }.csrf { it.disable() }
            .build()


}

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}

@Component
@RestController
@RequestMapping("/")
class WebController {
    @GetMapping("/")
    fun hello() = "hello world"
}
