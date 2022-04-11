package main;

import pokemon.OpenNotifyPokemonWebService;
import pokemon.PokemonBattle;

import java.util.Locale;
import java.util.stream.Stream;

public class PokemonInit {

  public static void main(String[] args) {

    PokemonBattle pokemonWebService = new PokemonBattle();
    pokemonWebService.setWebService(new OpenNotifyPokemonWebService());

    String[] listOfPokemon = Stream.of(args)
      .flatMap(a -> Stream.of(a.split(",")))
      .toArray(String[]::new);

    for (String pokemon : listOfPokemon) {
      System.out.println(pokemon + " receives double damage from: " +
          pokemonWebService.retrieveWeaknessesOfPokemon(
              pokemon.toLowerCase(Locale.ROOT)));
    }

  }
}
