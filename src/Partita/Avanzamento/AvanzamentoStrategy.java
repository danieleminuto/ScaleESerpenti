package Partita.Avanzamento;

import Giocatori.Giocatore;
import Partita.WinException;

public interface AvanzamentoStrategy {
    Giocatore avanza(Giocatore player, int caselle) throws WinException;
}
