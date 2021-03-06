package fr.jerep6.ogi.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import fr.jerep6.ogi.search.service.ServiceSearch;

@Component
public class EventHandlerUpdateRealProperty implements ApplicationListener<EventUpdateRealProperty> {

	@Autowired
	private ServiceSearch	serviceSearch;

	@Override
	public void onApplicationEvent(EventUpdateRealProperty event) {
		serviceSearch.index(event.getProperty());
	}

}
