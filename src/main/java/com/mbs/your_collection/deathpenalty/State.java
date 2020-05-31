package com.mbs.your_collection.deathpenalty;

public class State {
	private double drop=0.2;
	private double remove=0.3;
	private double dropFromPlayer=0.2;
	private double removeFromPlayer=0.3;

	/**
	 * 이전 버전 지원
	 * @param drop
	 * @param remove
	 */
	public State(double drop, double remove) {
		this.drop = drop;
		this.remove = remove;
		this.dropFromPlayer = drop;
		this.removeFromPlayer = remove;
	}

	public State(double drop, double remove, double dropFromPlayer, double removeFromPlayer) {
		this.drop = drop;
		this.remove = remove;
		this.dropFromPlayer = dropFromPlayer;
		this.removeFromPlayer = removeFromPlayer;
	}

	public double getDrop() {
		return drop;
	}

	public void setDrop(double drop) {
		this.drop = drop;
	}

	public double getRemove() {
		return remove;
	}

	public void setRemove(double remove) {
		this.remove = remove;
	}

	public double getDropFromPlayer() {
		return dropFromPlayer;
	}

	public void setDropFromPlayer(double dropFromPlayer) {
		this.dropFromPlayer = dropFromPlayer;
	}

	public double getRemoveFromPlayer() {
		return removeFromPlayer;
	}

	public void setRemoveFromPlayer(double removeFromPlayer) {
		this.removeFromPlayer = removeFromPlayer;
	}
}
