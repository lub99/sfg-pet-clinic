package guru.springframework.sfgpetclinic.model;

/**
 * Pet types cannot be too much and
 * I decided to use Integer type for id to create better architecture
 * because id can be of any type (Long, Int, String, etc.).
 * */
public class PetType extends BaseEntity<Integer>{
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
