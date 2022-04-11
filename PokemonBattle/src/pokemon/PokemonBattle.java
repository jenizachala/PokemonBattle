package pokemon;

import java.util.*;
import java.util.stream.Collectors;

public class PokemonBattle {

  private PokemonWebService pokemonWebService;

  public void setWebService(PokemonWebService webService) {
    pokemonWebService = webService;
  }

  public List<String> searchForDuplicateWeaknesses(List<String> weaknesses) {

    Set<String> uniqueTypes = new HashSet<>();

    List<String> duplicateWeaknesses = weaknesses.stream()
        .filter(i -> !uniqueTypes.add(i))
        .collect(Collectors.toList());

    return (!duplicateWeaknesses.isEmpty()) ? duplicateWeaknesses : weaknesses;
  }

  public List<String> eliminateImmunitiesFromWeaknesses(
      List<Map<String, List<String>>> allInformation) {

    List<String> immunities = new ArrayList<>();
    List<String> weaknesses = new ArrayList<>();

    for (Map<String, List<String>> type : allInformation) {
      immunities.addAll(type.get("immunities"));
      weaknesses.addAll(type.get("weaknesses"));
    }

    immunities.forEach(weaknesses::remove);

    return searchForDuplicateWeaknesses(weaknesses);
  }

  public List<String> retrieveWeaknessesOfPokemon(String pokemonName) {

    String s = "pokemon/" + pokemonName + "/";
    List<Map<String, List<String>>> allInformation = new ArrayList<>();

    try {
      List<String> pokemonTypes = pokemonWebService.fetchTypeOfPokemon(s);

      for (String type : pokemonTypes) {
        Map<String, List<String>> informationOfType =
            pokemonWebService.fetchInformationOfType(type);
        allInformation.add(informationOfType);
      }

      return eliminateImmunitiesFromWeaknesses(allInformation);
    } catch (Exception ex) {
      return Collections.singletonList(ex.getMessage());
    }
  }

}
