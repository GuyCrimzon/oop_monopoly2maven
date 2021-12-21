package project.model;

public enum PlayerStatus {
    NORMAL(0),
    REVERSED(1),
    STOP(2),
    LOST(3);

    int status;
    PlayerStatus(int status) {
        this.status = status;
    }
}
