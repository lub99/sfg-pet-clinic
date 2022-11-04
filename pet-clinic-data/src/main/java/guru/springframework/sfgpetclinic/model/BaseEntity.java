package guru.springframework.sfgpetclinic.model;

import java.io.Serializable;

public class BaseEntity<ID> implements Serializable {
    private ID id;

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }
}
