package pokemon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class PokemonBattleTests {

  PokemonBattle pokemonBattle;
  PokemonWebService webService;

  @Test
  void Canary() {
    assertTrue(true);
  }

  @BeforeEach
  void initiateMocksForTests() {
    webService = mock(PokemonWebService.class);
    pokemonBattle = new PokemonBattle();
    pokemonBattle.setWebService(webService);
  }

  @Test
  void aListWithDuplicatesShouldReturnOnlyDuplicates() {
    List<String> listWithDuplicates =
        List.of("ground","rock","water","rock","electric","ice");

    assertEquals(List.of("rock"),
        pokemonBattle.searchForDuplicateWeaknesses(listWithDuplicates));
  }

  @Test
  void aListWithoutDuplicatesShouldReturnOriginalList() {
    List<String> listWithoutDuplicates =
        List.of("ground","rock","water","electric","ice");

    assertEquals(listWithoutDuplicates,
        pokemonBattle.searchForDuplicateWeaknesses(listWithoutDuplicates));
  }

  @Test
  void immunitiesPresentInWeaknessesShouldBeRemoved() {
    List<Map<String, List<String>>> allInformation =
        List.of(
            Map.of("weaknesses", List.of("ground","water"),
                "immunities", List.of("")),
            Map.of("weaknesses", List.of("rock","electric","ice"),
                "immunities", List.of("ground"))
        );

    assertEquals(List.of("water","rock","electric","ice"),
        pokemonBattle.eliminateImmunitiesFromWeaknesses(allInformation));
  }

  @Test
  void originalListShouldReturnIfNoImmunitiesPresent() {
    List<Map<String, List<String>>> allInformation =
        List.of(
            Map.of("weaknesses", List.of("psychic","water"),
                "immunities", List.of("")),
            Map.of("weaknesses", List.of("rock","electric","ice"),
                "immunities", List.of("ground"))
        );

    assertEquals(List.of("psychic","water","rock","electric","ice"),
        pokemonBattle.eliminateImmunitiesFromWeaknesses(allInformation));
  }

  @Test
  void retrieveWeaknessesOfPokemonReturnsListOfWeaknessesBasedOnType() {
    String nameOfPokemon = "charizard";
    List<String> types = List.of("10", "3");

    Map<String, List<String>> informationOfType10 = new HashMap<>() {{
      put("weaknesses", List.of("ground","rock","water"));
      put("immunities", List.of(""));
    }};

    Map<String, List<String>> informationOfType3 = new HashMap<>() {{
      put("weaknesses", List.of("rock","electric","ice"));
      put("immunities", List.of("ground"));
    }};

    when(webService.fetchTypeOfPokemon("pokemon/" + nameOfPokemon + "/")).thenReturn(types);
    when(webService.fetchInformationOfType("10")).thenReturn(informationOfType10);
    when(webService.fetchInformationOfType("3")).thenReturn(informationOfType3);

    assertEquals(List.of("rock"),
        pokemonBattle.retrieveWeaknessesOfPokemon(nameOfPokemon));
  }

  @Test
  void retrieveWeaknessesOfPokemonReportsWebServiceError() {
    when(webService.fetchTypeOfPokemon("pokemon/pikachu/"))
        .thenThrow(new RuntimeException("Web Service Error"));

    assertEquals(List.of("Web Service Error"),
        pokemonBattle.retrieveWeaknessesOfPokemon("pikachu"));
  }

  @Test
  void retrieveWeaknessesOfPokemonReportsNetworkError() {
    when(webService.fetchTypeOfPokemon("pokemon/pikachu/"))
        .thenThrow(new RuntimeException("Network Failure"));

    assertEquals(List.of("Network Failure"),
        pokemonBattle.retrieveWeaknessesOfPokemon("pikachu"));
  }
}
