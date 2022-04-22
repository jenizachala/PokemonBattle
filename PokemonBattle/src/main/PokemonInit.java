package main;

import pokemon.OpenNotifyPokemonWebService;
import pokemon.PokemonBattle;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PokemonInit extends JFrame implements ActionListener {

  static String pokemonName;
  static JTextField textBox;
  static JTextArea responseSpace;
  JButton submitButton;

  static PokemonBattle pokemonWebService;

  public void actionPerformed(ActionEvent e) {
    String s = e.getActionCommand();

    if (s.equals("Submit")) {
      pokemonName = textBox.getText().replaceAll("\\s", "-");
      List<String> weaknesses =
          pokemonWebService.retrieveWeaknessesOfPokemon(pokemonName.toLowerCase());
      responseSpace.setText("Weak to: \n" + weaknesses.toString());
    }
  }

  public PokemonInit() {
    JLabel instructions = new JLabel("Enter Pokemon Name: ", JLabel.CENTER);
    instructions.setBorder(new EmptyBorder(20, 0, 0, 0));

    textBox = new JTextField();
    textBox.setColumns(20);
    textBox.setBackground(Color.lightGray);
    textBox.setForeground(Color.darkGray);

    submitButton = new JButton("Submit");
    submitButton.addActionListener(this);

    responseSpace = new JTextArea("Weak to: \n");
    responseSpace.setLineWrap(true);
    responseSpace.setWrapStyleWord(true);
    responseSpace.setEditable(false);
    responseSpace.setOpaque(false);
    responseSpace.setSize(275,50);

    JPanel panel = new JPanel();
    panel.add(instructions);
    panel.add(textBox);
    panel.add(submitButton);
    panel.add(responseSpace);

    add(panel);
  }

  public static void main(String[] args) {

    pokemonWebService = new PokemonBattle();
    pokemonWebService.setWebService(new OpenNotifyPokemonWebService());

    PokemonInit frame = new PokemonInit();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setBackground(Color.white);
    frame.setSize(300, 200);
    frame.setVisible(true);

  }
}

//package main;
//
//import pokemon.OpenNotifyPokemonWebService;
//import pokemon.PokemonBattle;
//
//import java.util.Locale;
//import java.util.stream.Stream;
//
//public class PokemonInit {
//
//  public static void main(String[] args) {
//
//    PokemonBattle pokemonWebService = new PokemonBattle();
//    pokemonWebService.setWebService(new OpenNotifyPokemonWebService());
//
//    String[] listOfPokemon = Stream.of(args)
//        .flatMap(a -> Stream.of(a.split(",")))
//        .toArray(String[]::new);
//
//    for (String pokemon : listOfPokemon) {
//      System.out.println(pokemon + " receives double damage from: " +
//          pokemonWebService.retrieveWeaknessesOfPokemon(
//              pokemon.toLowerCase(Locale.ROOT)));
//    }
//
//  }
//}