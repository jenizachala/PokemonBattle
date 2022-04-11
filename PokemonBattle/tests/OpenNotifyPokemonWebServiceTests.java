package pokemon;

import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class OpenNotifyPokemonWebServiceTests {

  OpenNotifyPokemonWebService webService, spyWebService;

  public static String readFileAsString(String file) throws Exception {
    return new String(Files.readAllBytes(Paths.get(file)));
  }

  @BeforeEach
  void initiateClassesAndMocks() {
    webService = new OpenNotifyPokemonWebService();
    spyWebService = Mockito.spy(OpenNotifyPokemonWebService.class);
  }

  @Test
  void parseDataAndReturnTypesOfPokemon() throws Exception {
    String fileName1 = "PokemonBattle/tests/charizard.json";
    String charizard = readFileAsString(fileName1);

    String fileName2 = "PokemonBattle/tests/ditto.json";
    String ditto = readFileAsString(fileName2);

    assertAll(
        () -> assertEquals(List.of("type/10/","type/3/"),
            webService.parseJSONDataForTypes(charizard)),
        () -> assertEquals(List.of("type/1/"),
            webService.parseJSONDataForTypes(ditto))
    );
  }

  @Test
  void parseDataAndReturnInformationOfTypes() throws Exception {
    String fileName1 = "PokemonBattle/tests/type10.json";
    String type10 = readFileAsString(fileName1);

    String fileName2 = "PokemonBattle/tests/type3.json";
    String type3 = readFileAsString(fileName2);

    Map<String, List<String>> type10Information =
        Map.of("weaknesses", List.of("ground","rock","water"),
            "immunities", List.of());

    Map<String, List<String>> type3Information =
        Map.of("weaknesses", List.of("rock","electric","ice"),
            "immunities", List.of("ground"));

    assertAll(
        () -> assertEquals(type10Information,
            webService.parseJSONDataForTypeInfo(type10)),
        () -> assertEquals(type3Information,
            webService.parseJSONDataForTypeInfo(type3))
    );
  }

  @Test
  void fetchTypeOfPokemonPassesResponseToParseFunction() throws Exception {
    String fileName = "PokemonBattle/tests/charizard.json";
    String charizard = readFileAsString(fileName);

    spyWebService.callPokemonService("pokemon/charizard/");
    spyWebService.parseJSONDataForTypes(charizard);

    Mockito.verify(spyWebService).callPokemonService("pokemon/charizard/");
    Mockito.verify(spyWebService).parseJSONDataForTypes(charizard);
  }

  @Test
  void fetchInformationOfTypePassesResponseToParseFunction() throws Exception {
    String fileName = "PokemonBattle/tests/type10.json";
    String type10 = readFileAsString(fileName);

    spyWebService.callPokemonService("type/10/");
    spyWebService.parseJSONDataForTypeInfo(type10);

    Mockito.verify(spyWebService).callPokemonService("type/10/");
    Mockito.verify(spyWebService).parseJSONDataForTypeInfo(type10);
  }

  @Test
  void fetchTypeOfPokemonThrowsErrorIfPokemonNotFound() {
    Exception exception = assertThrows(Exception.class,
        () -> webService.fetchTypeOfPokemon("pokemon/charizar/"));

    assertEquals("Not Found", exception.getMessage());
  }

  @Test
  void callPokemonServiceThrowsNetworkError() throws Exception {
    when(spyWebService.callPokemonService("pokemon/charizard/"))
        .thenThrow(new IOException("Network Error"));

    RuntimeException exception = assertThrows(RuntimeException.class,
        () -> spyWebService.fetchTypeOfPokemon("pokemon/charizard/"));
    assertTrue(exception.getMessage().contains("Network Error"));
  }

  @Test
  void fetchInformationOfTypeThrowsErrorIfPokemonNotFound() {
    Exception exception = assertThrows(Exception.class,
        () -> webService.fetchInformationOfType("type/22/"));

    assertEquals("Not Found", exception.getMessage());
  }

  @Test
  void fetchTypeOfPokemonReturnsListOfTypes() {
    assertEquals(List.of("type/10/","type/3/"),
        webService.fetchTypeOfPokemon("pokemon/charizard/"));
  }

  @Test
  void fetchInformationOfTypeReturnsWeaknessesAndImmunities() {
    Map<String, List<String>> type10Information =
        Map.of("weaknesses", List.of("ground","rock","water"),
            "immunities", List.of());

    assertEquals(type10Information,
        webService.fetchInformationOfType("type/10/"));
  }
}
