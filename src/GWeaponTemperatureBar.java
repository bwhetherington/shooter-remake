import java.awt.Color;
import java.awt.Graphics;


public class GWeaponTemperatureBar extends GComponent {
	private Weapon target;
	boolean isAutoShown;
	
	public GWeaponTemperatureBar(Weapon target) {
		super(256, 12);
		this.target = target;
		target.addWeaponListener(new WeaponAdapter() {
			public void heatUp(WeaponEvent event) {
				if (isAutoShown() && isAutoShown()) {
					setVisible(true);
				}
			}
			
			public void coolDown(WeaponEvent event) {
				if (isAutoShown() && isAutoShown()) {
					setVisible(false);
				}
			}
		});
		setAutoShown(true);
	}
	
	public void setAutoShown(boolean isAutoShown) {
		this.isAutoShown = isAutoShown;
		if (isAutoShown) {
			setVisible(target.getTemperature() > 0);
		}
	}
	
	public boolean isAutoShown() {
		return isAutoShown;
	}
	
	public void renderComponent(Graphics g) {
		super.renderComponent(g);
		g.setColor(target.getTemperature() < target.getTemperatureFire() ? Colors.TEMPERATURE_BAR_FOREGROUND : Colors.TEMPERATURE_BAR_OVERHEATED_FOREGROUND);
		g.fillRect(0, 0, (int) (target.getTemperature() / target.getTemperatureFire() * getWidth()), getHeight());
	}
	
}
