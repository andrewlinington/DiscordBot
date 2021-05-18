package main.utils;

import Commands.SH.utils.Gamestate;
import Commands.SH.utils.PlayerList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



/**
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SHGame {
  private Gamestate gamestate;
  private PlayerList lobby;
  private FileConfig config;
}
