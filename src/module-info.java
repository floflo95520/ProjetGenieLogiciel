module Projet {
	requires javafx.controls;
	requires java.desktop;
	requires javafx.graphics;
	requires javafx.swing;
	opens application to javafx.graphics, javafx.fxml;
}
