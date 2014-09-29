package net.nym.library.task;

/**
 * @author nym
 * @date 2014/9/29 0029.
 */
public interface TaskHandler {

    boolean supportPause();

    boolean supportResume();

    boolean supportCancel();

    void pause();

    void resume();

    boolean cancel();

    boolean isPaused();

    boolean isCancelled();

    boolean isFinished();
}
