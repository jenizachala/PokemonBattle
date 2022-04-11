package pokemon;

import java.util.List;
import java.util.Map;

public interface PokemonWebService {
  List<String> fetchTypeOfPokemon(String pokemonName);

  Map<String, List<String>> fetchInformationOfType(String type);
}
