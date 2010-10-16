package net.opgenorth.yeg.vote.model;
public interface ITransmorgifier<FROM, TO> {
	TO transmorgify(FROM source);
}