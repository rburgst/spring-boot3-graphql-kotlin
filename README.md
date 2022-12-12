# Sample for spring boot 3 with kotlin

This sample shows problems with hibernate l1 caching
with spring boot 3, hibernate 6 and kotlin entities

I have implemented the same application 1:1 in java
and there everything works.

To see the problem

1. `gradlew booRun`
2. Open the browser at http://localhost:8080/graphiql
3. Enter the following graphql query
   ```graphql
   query {
      allClubs {
         id
         clubName
         athletes {
           firstName
         }
      }
   }
   ```


## Expected 
the correct response should be produced

## Actual

```json
{
  "errors": [
    {
      "message": "The field at path '/allClubs[0]/athletes' was declared as a non null type, but the code involved in retrieving data has wrongly returned a null value.  The graphql specification requires that the parent field be set to null, or if that is non nullable that it bubble up null to its parent and so on. The non-nullable type is '[Athlete!]' within parent type 'Club'",
      "path": [
        "allClubs",
        0,
        "athletes"
      ],
      "extensions": {
        "classification": "NullValueInNonNullableField"
      }
    },
    {
      "message": "The field at path '/allClubs[1]/athletes' was declared as a non null type, but the code involved in retrieving data has wrongly returned a null value.  The graphql specification requires that the parent field be set to null, or if that is non nullable that it bubble up null to its parent and so on. The non-nullable type is '[Athlete!]' within parent type 'Club'",
      "path": [
        "allClubs",
        1,
        "athletes"
      ],
      "extensions": {
        "classification": "NullValueInNonNullableField"
      }
    }
  ],
  "data": null
}
```

As you can see from the source I already tried my luck with
lots of compiler plugins, none helped so far.

To see the problem in more detail put a breakpoint into 
```kotlin
    @BatchMapping(typeName = "Club")
    fun athletes(clubs: List<Club>): Map<Club, List<Athlete>> {
        val clubIdList = clubs.map { it.id!! }.toList()
        val athletes = athleteRepository.findAthletesByClubIdIn(clubIdList)
        val result = athletes
            .groupBy { it.club!! }
        return result
    }
```

just before the `return result`. You will see that within the athletes the club entities are
still proxies even though they should already be fetched from the l1 cache (see parameter `clubs`).
