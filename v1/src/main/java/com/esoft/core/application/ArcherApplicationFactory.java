package com.esoft.core.application;

import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;

/**  
 * Company:     jdp2p <br/> 
 * Copyright:   Copyright (c)2013 <br/>
 * Description:  
 * @author:     wangzhi  
 * @version:    1.0
 * Create at:   2014-1-13 上午9:47:05  
 *  
 * Modification History: <br/>
 * Date         Author      Version     Description  
 * ------------------------------------------------------------------  
 * 2014-1-13      wangzhi      1.0          
 */


public class ArcherApplicationFactory extends ApplicationFactory {

	// Variables ------------------------------------------------------------------------------------------------------

	private final ApplicationFactory wrapped;
	private volatile Application application;

	// Constructors ---------------------------------------------------------------------------------------------------

	/**
	 * Construct a new OmniFaces application factory around the given wrapped factory.
	 * @param wrapped The wrapped factory.
	 */
	public ArcherApplicationFactory(ApplicationFactory wrapped) {
		this.wrapped = wrapped;
	}

	// Actions --------------------------------------------------------------------------------------------------------

	/**
	 * Returns a new instance of {@link OmniApplication} which wraps the original application.
	 */
	@Override
	public Application getApplication() {
		if (application == null) {
			application = new ArcherApplication(wrapped.getApplication());
		}

		return application;
	}

	/**
	 * Sets the given application instance as the current instance. If it's not an instance of {@link OmniApplication},
	 * then it will be wrapped by {@link OmniApplication}.
	 */
	@Override
	public synchronized void setApplication(Application application) {
		this.application = (application instanceof ArcherApplication) ? application : new ArcherApplication(application);
		wrapped.setApplication(this.application);
	}

	/**
	 * Returns the wrapped factory.
	 */
	@Override
	public ApplicationFactory getWrapped() {
		return wrapped;
	}

}