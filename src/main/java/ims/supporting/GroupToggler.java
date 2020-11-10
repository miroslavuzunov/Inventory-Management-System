package ims.supporting;

import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

import java.util.List;

public class GroupToggler {
    public static ToggleGroup makeToggleGroup(List<RadioButton> buttons){
        ToggleGroup toggleGroup = new ToggleGroup();

        buttons.forEach(button ->{
            button.setToggleGroup(toggleGroup);
        });

        toggleGroup.selectToggle(buttons.get(1)); //default select

        return toggleGroup;
    }
}
