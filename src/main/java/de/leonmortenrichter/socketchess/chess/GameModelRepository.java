package de.leonmortenrichter.socketchess.chess;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameModelRepository extends JpaRepository<GameModel, Long> {

    GameModel findFirstByHasEmptySlot(boolean hasEmptySlot);

    GameModel findByPlayer1IdOrPlayer2Id(String id1, String id2);


}
