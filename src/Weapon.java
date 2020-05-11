import java.util.Date;
import java.util.HashSet;
import java.util.Set;


public abstract class Weapon {
	private ActionTimer
		timer,
		cooldownTimer;
	private Set<WeaponListener> listeners;
	private double
		temperatureMax,
		temperature,
		temperatureFire,
		temperatureTrigger,
		kickback;
	private boolean
		isCool,
		isEnabled,
		isOverheated;
	
	private static final int
		FIRE_START_EVENT = 0,
		FIRE_STOP_EVENT = 1,
		FIRE_EVENT = 2;
	
	private static final double
		TEMPERATURE_COOLDOWN_RATE = 0.006;
	
	public Weapon(double delay, double temperatureMax, double temperatureFire, double temperatureTrigger) {
		this.temperatureMax = temperatureMax;
		this.temperatureFire = temperatureFire;
		this.temperatureTrigger = temperatureTrigger;
		timer = new ActionTimer(delay, ActionTimer.REPEAT, () -> {
			fireWeapon();
		});
		cooldownTimer = new ActionTimer(1, ActionTimer.REPEAT, () -> {
			increaseTemperature(-TEMPERATURE_COOLDOWN_RATE * temperatureMax);
		});
		cooldownTimer.start();
		timer.setTimeScale(1);
		listeners = new HashSet<>();
		isCool = temperature <= 0;
		isEnabled = true;
	}
	
	public Weapon(double delay) {
		this(delay, 100, 80, 5);
	}
	
	public void start() {
		timer.start();
		dispatchEvent(new WeaponEvent(this, WeaponEvent.Type.FIRE_START));
	}
	
	public void stop() {
		timer.stop();
		dispatchEvent(new WeaponEvent(this, WeaponEvent.Type.FIRE_STOP));
	}
	
	private void fireWeapon() {
		if (isEnabled && temperature < temperatureFire) {
			fire();
			increaseTemperature(temperatureTrigger);
			dispatchEvent(new WeaponEvent(this, WeaponEvent.Type.FIRE));
		}
	}
	
	private void increaseTemperature(double inc) {
		temperature += inc;
		if (temperature >= temperatureMax) {
			temperature = temperatureMax;
		}
		
		if (!isOverheated && temperature > temperatureFire) {
			temperature = temperatureMax;
			isOverheated = true;
			dispatchEvent(new WeaponEvent(this, WeaponEvent.Type.OVERHEAT));
		}
		
		boolean temp = isCool;
		if (temperature <= 0) {
			temperature = 0;
			isCool = true;
		} else if (temperature > 0) {
			isCool = false;
		}
		if (temp != isCool) {
			dispatchEvent(new WeaponEvent(this, isCool ? WeaponEvent.Type.COOL_DOWN : WeaponEvent.Type.HEAT_UP));
		}
	}
	
	public abstract void fire();
	
	public void update() {		
		// Ensure temperature is within bounds
		if (temperature < 0) {
			temperature = 0;
		} else if (temperature > temperatureMax) {
			temperature = temperatureMax;
		} else if (temperature < temperatureFire) {
			isOverheated = false;
		}
	}
	
	private void dispatchEvent(WeaponEvent event) {
		for (WeaponListener listener : listeners) {
			switch (event.getType()) {
			case FIRE_START:
				listener.fireStart(event);
				break;
			case FIRE_STOP:
				listener.fireStop(event);
				break;
			case FIRE:
				listener.fire(event);
				break;
			case OVERHEAT:
				listener.overheat(event);
				break;
			case COOL_DOWN:
				listener.coolDown(event);
				break;
			case HEAT_UP:
				listener.heatUp(event);
				break;
			}
		}
	}
	
	public void addWeaponListener(WeaponListener listener) {
		listeners.add(listener);
	}
	
	public double getTemperature() {
		return temperature;
	}
	
	public double getTemperatureMax() {
		return temperatureMax;
	}
	
	public double getTemperatureFire() {
		return temperatureFire;
	}
	
	public void setKickback(double kickback) {
		this.kickback = kickback;
	}
	
	public double getKickback() {
		return kickback;
	}
	
	public void destroy() {
		isEnabled = false;
		timer.destroy();
		cooldownTimer.destroy();
	}
}
