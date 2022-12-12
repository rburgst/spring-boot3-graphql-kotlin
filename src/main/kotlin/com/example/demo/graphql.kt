package com.example.demo

import jakarta.transaction.Transactional
import mu.KotlinLogging
import org.springframework.graphql.data.method.annotation.BatchMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

private val logger = KotlinLogging.logger {}

@Controller
 @Transactional
class GraphqlQueryResolver(val clubRepository: ClubRepository, val athleteRepository: AthleteRepository) {
    @QueryMapping
    fun allClubs(): MutableList<Club> {
        val allClubs = clubRepository.findAll()
        logger.info { "all clubs $allClubs" }
        return allClubs
    }

    @QueryMapping
    fun allAthletes() = athleteRepository.findAll()

    @BatchMapping(typeName = "Club")
    fun athletes(clubs: List<Club>): Map<Club, List<Athlete>> {
        val clubIdList = clubs.map { it.id!! }.toList()
        val athletes = athleteRepository.findAthletesByClubIdIn(clubIdList)
        val result = athletes
            .groupBy { it.club!! }
        return result
    }
}
