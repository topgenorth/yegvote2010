package net.opgenorth.yeg.vote;
public interface ITransmorgifier<FROM, TO> {
	TO transmorgify(FROM source);
}