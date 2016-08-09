package com.speedment.tool.property.item;

import com.speedment.runtime.annotation.Api;
import com.speedment.runtime.util.StaticClassUtil;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

/**
 *
 * @author Simon Jonasson
 * @since 3.0.0
 */
@Api(version = "3.0")
public class ItemUtil {

    public static final String DATABASE_RELATION_TOOLTIP = 
        "This field should ONLY be changed to reflect changes made in the underlying database.\nEnable editing by by right clicking on the field.";

    /**
     * Makes a node disabled by default, and requires that the user right-clicks
     * the node to enable it.
     * <p>
     * This decorator is intended to use on fields that can be edited, but
     * should only be edited under certain conditions, such as the Table.Name
     * field
     *
     * @param node          the node to decorate
     * @param tooltipText   the tooltip text displayed while the node is disabled
     * @return              the decorated node, which will be wrapped in a StackPane
     */
    public static Node lockDecorator(Node node, String tooltipText) {
        node.setDisable(true);

        final StackPane pane = new StackPane();
        final ContextMenu menu = new ContextMenu();
        final MenuItem item = new MenuItem("Enable editing");
        final Tooltip tooltip = new Tooltip(tooltipText);
        
        Tooltip.install(pane, tooltip);
        menu.getItems().add(item);

        final EventHandler<MouseEvent> contextMenuToggle = (MouseEvent event) -> {
            if (event.isSecondaryButtonDown() && !menu.isShowing()) {
                menu.show(pane, event.getScreenX(), event.getScreenY());
            } else if (menu.isShowing()) {
                menu.hide();
            }
            event.consume();
        };
        final EventHandler<ActionEvent> menuItemClicked = (ActionEvent event) -> {
            Tooltip.uninstall(pane, tooltip);
            pane.removeEventHandler(MouseEvent.MOUSE_PRESSED, contextMenuToggle);
            node.setDisable(false);
        };
        item.setOnAction(menuItemClicked);

        pane.setOnMousePressed(contextMenuToggle);
        pane.getChildren().add(node);
        return pane;
    }

    private ItemUtil() {
        StaticClassUtil.instanceNotAllowed(ItemUtil.class);
    }
}