package guru.springframework.sfgpetclinic.model;

public class PetType extends BaseEntity<Integer>{
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
