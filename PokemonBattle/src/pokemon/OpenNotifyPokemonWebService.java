package pokemon;

import java.net.URL;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import static java.util.stream.Collectors.joining;

public class OpenNotifyPokemonWebService implements PokemonWebService {

  public List<String> parseJSONDataForTypes(String jsonString) {
    JSONObject jsonText = new JSONObject(jsonString);

    List<String> types = new ArrayList<>();
    int numberOfTypes = jsonText.getJSONArray("types").length();

    for (int i = 0; i < numberOfTypes; i++) {
      types.add(
          StringUtils.truncate(
              jsonText.getJSONArray("types")
                  .getJSONObject(i)
                  .getJSONObject("type")
                  .getString("url"),26,8)
      );
    }

    return types;
  }

  public Map<String, List<String>> parseJSONDataForTypeInfo(String jsonString) {
    JSONObject jsonText = new JSONObject(jsonString);
    Map<String, List<String>> typeInformation = new HashMap<>();

    int lengthOfWeaknesses = jsonText.getJSONObject("damage_relations")
        .getJSONArray("double_damage_from").length();

    List<String> weaknesses = new ArrayList<>();
    for (int i = 0; i < lengthOfWeaknesses; i++) {
      weaknesses.add(
          jsonText.getJSONObject("damage_relations")
              .getJSONArray("double_damage_from")
              .getJSONObject(i)
              .getString("name"));
    }

    int lengthOfImmunities = jsonText.getJSONObject("damage_relations")
        .getJSONArray("no_damage_from").length();

    List<String> immunities = new ArrayList<>();
    for (int i = 0; i < lengthOfImmunities; i++) {
      immunities.add(
          jsonText.getJSONObject("damage_relations")
              .getJSONArray("no_damage_from")
              .getJSONObject(i)
              .getString("name"));
    }

    typeInformation.put("weaknesses", weaknesses);
    typeInformation.put("immunities", immunities);

    return typeInformation;
  }

  public String callPokemonService(String s) throws Exception {
    String url = "https://pokeapi.co/api/v2/" + s;

    try {
      return new java.util.Scanner(
          new URL(url).openStream()).tokens().collect(joining(""));
    } catch(Exception ex) {
      throw new RuntimeException("Not Found");
    }
  }

  public Map<String, List<String>> fetchInformationOfType(String type) {
    String urlResponse;
        Map<String, List<String>> parsedResponse;

    try {
      urlResponse = callPokemonService(type);
      parsedResponse = parseJSONDataForTypeInfo(urlResponse);
    } catch(Exception ex) {
      throw new RuntimeException(ex.getMessage());
    }
    return parsedResponse;
  }

  public List<String> fetchTypeOfPokemon(String pokemonName) {
    String urlResponse;
    List<String> parsedResponse;

    try {
      urlResponse = callPokemonService(pokemonName);
      parsedResponse = parseJSONDataForTypes(urlResponse);
    } catch(Exception ex) {
      throw new RuntimeException(ex.getMessage());
    }
    return parsedResponse;
  }

}
