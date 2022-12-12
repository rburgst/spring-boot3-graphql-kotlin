package com.example.demo

import com.github.f4b6a3.tsid.Tsid
import com.github.f4b6a3.tsid.TsidCreator
import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Entity
open class Club(
    @Column
    open var clubName: String,
    @Column
    @Id
    open val id: Tsid? = TsidCreator.getTsid1024(),
) {
    constructor() : this("")

    override fun toString(): String {
        return "Club $clubName ($id)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Club

        if (id == null || id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

}

@Entity
open class Athlete(
    @ManyToOne(fetch = FetchType.LAZY)
    open var club: Club?,
    @Column
    open var lastName: String,
    @Column
    open var firstName: String,
    @Column
    open var gender: Gender,
    @Column
    @Id
    open var id: Tsid? = TsidCreator.getTsid1024(),
) {
    constructor() : this(null, "", "", Gender.FEMALE)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Athlete

        if (id == null || id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}

enum class Gender {
    FEMALE, MALE
}

@Repository
interface ClubRepository : JpaRepository<Club, Tsid> {}

@Repository
interface AthleteRepository : JpaRepository<Athlete, Tsid> {
    fun findAthletesByClub_Id(id: Tsid): List<Athlete>
    fun findAthletesByClubIdIn(idList: List<Tsid>): List<Athlete>
}
