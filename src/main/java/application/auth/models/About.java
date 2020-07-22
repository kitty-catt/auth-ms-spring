package application.auth.models;

import lombok.Getter;
import lombok.Setter;

public class About {

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String parentRepo;

    @Getter
    @Setter
    private String description;

    public About(String name, String parentRepo, String description) {
        this.setName(name);
        this.setParentRepo(parentRepo);
        this.setDescription(description);
    }

    @Override
    public String toString() {
        return "About [name=" + name + ", parentRepo=" + parentRepo + ", description=" + description + "]";
    }
}
