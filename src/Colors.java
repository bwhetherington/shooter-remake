import java.awt.Color;


public final class Colors {
	
	public static final Color
		LIFE_BAR_FOREGROUND = new Color(0, 200, 0, 192),
		TEMPERATURE_BAR_FOREGROUND = new Color(192, 0, 0, 192),
		TEMPERATURE_BAR_OVERHEATED_FOREGROUND = new Color(255, 255, 255, 192),
		SHIELDS_BAR_FOREGROUND = new Color(36, 168, 192, 192),
		BAR_BACKGROUND = new Color(0, 0, 0, 192),
		OUTLINE = new Color(255, 255, 255),
		ENEMY_COLOR = new Color(192, 0, 0, 192),
		TEXT_SHADOW = new Color(0, 0, 0, 96),
		UI_COLOR = new Color(12, 48, 64, 192),
		UI_BORDER = Util.getBorderColor(UI_COLOR),
		ENEMY_UI_COLOR = new Color(64, 0, 0, 192),
		ENEMY_UI_BORDER = Util.getBorderColor(new Color(162, 0, 0, 192)),
		ENEMY_LIFE_BAR_FOREGROUND = new Color(192, 0, 0, 192),
		
		DARK_FILL = new Color(0, 0, 0, 128),
		TRANSPARENT = new Color(0, 0, 0, 0);
	
	private Colors() {} 
	
}
