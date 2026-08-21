package co.eci.snake.core;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.atomic.AtomicLong;

public final class Snake {
  private static final AtomicLong deathCounter = new AtomicLong(0);

  private final Deque<Position> body = new ArrayDeque<>();
  private volatile Direction direction;
  private int maxLength = 5;
  private volatile boolean alive = true;
  private volatile long deathOrder = -1;

  private Snake(Position start, Direction dir) {
    body.addFirst(start);
    this.direction = dir;
  }

  public static Snake of(int x, int y, Direction dir) {
    return new Snake(new Position(x, y), dir);
  }

  public Direction direction() { return direction; }

  public void turn(Direction dir) {
    if ((direction == Direction.UP && dir == Direction.DOWN) ||
        (direction == Direction.DOWN && dir == Direction.UP) ||
        (direction == Direction.LEFT && dir == Direction.RIGHT) ||
        (direction == Direction.RIGHT && dir == Direction.LEFT)) {
      return;
    }
    this.direction = dir;
  }

  public synchronized Position head() { return body.peekFirst(); }

  public synchronized Deque<Position> snapshot() { return new ArrayDeque<>(body); }

  public synchronized void advance(Position newHead, boolean grow) {
    body.addFirst(newHead);
    if (grow) maxLength++;
    while (body.size() > maxLength) body.removeLast();
  }

  public synchronized int length() { return body.size(); }

  public boolean isAlive() { return alive; }

  public long deathOrder() { return deathOrder; }

  public synchronized void kill() {
    if (alive) {
      alive = false;
      deathOrder = deathCounter.incrementAndGet();
    }
  }
}
