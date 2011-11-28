package core.compoundterm;

public class Plugged extends CompoundTerm{

	public Plugged(CompoundTerm c1, CompoundTerm c2) {
		super(c1.getSources_uri(),c1.getReceivers_uri());
		addSources_uri(c2.getSources_uri());
		addReceivers_uri(c2.getReceivers_uri());
	}
}
