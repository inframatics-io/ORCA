package io.opensystems.rcca;

public final class TeamMember {
public String Name;
public String Email;
private int ID;

public TeamMember(String name, String email, int iD) {
	super();
	Name = name;
	Email = email;
	ID = iD;
}

public boolean isEqual(int i){
	return i==ID;
}
@Override
public String toString() {
	return Name;
}

}
