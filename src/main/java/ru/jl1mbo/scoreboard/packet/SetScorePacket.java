package ru.jl1mbo.scoreboard.packet;

import cn.nukkit.network.protocol.DataPacket;
import ru.jl1mbo.scoreboard.packet.entry.ScoreEntry;

import java.util.ArrayList;
import java.util.List;

public class SetScorePacket extends DataPacket {

	public static final int
			TYPE_CHANGE = 0,
			TYPE_REMOVE = 1;

	public byte type;
	public List<ScoreEntry> entries = new ArrayList<>();

	@Override
	public byte pid() {
		return 0x6c;
	}

	@Override
	public void decode() {/**/}

	@Override
	public void encode() {
		this.reset();
		this.putByte(this.type);
		this.putUnsignedVarInt(this.entries.size());
		for (ScoreEntry entry : this.entries) {
			this.putVarLong(entry.scoreboardId);
			this.putString("objective");
			this.putLInt(entry.score);
			if (this.type != TYPE_REMOVE) {
				this.putByte((byte) entry.type);
				switch (entry.type) {
					case ScoreEntry.TYPE_PLAYER:
					case ScoreEntry.TYPE_ENTITY:
						this.putUnsignedVarLong(entry.entityUniqueId);
						break;
					case ScoreEntry.TYPE_FAKE_PLAYER:
						this.putString(entry.customName);
						break;
					default:
						throw new IllegalArgumentException("Unknown entry " + entry.type);
				}
			}
		}
	}
}