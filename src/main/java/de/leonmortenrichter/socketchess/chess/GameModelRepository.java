package de.leonmortenrichter.socketchess.chess;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameModelRepository extends JpaRepository<GameModel, Long> {

    public List<GameModel> findByPlayer2IsNull();

    public GameModel findFirstByPlayer2IsNull();
}
