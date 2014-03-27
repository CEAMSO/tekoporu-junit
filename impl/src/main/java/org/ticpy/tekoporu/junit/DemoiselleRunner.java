/*
 * TICPY Framework
 * Copyright (C) 2012 Plan Director TICs
 *
----------------------------------------------------------------------------
 * Originally developed by SERPRO
 * Demoiselle Framework
 * Copyright (C) 2010 SERPRO
 *
----------------------------------------------------------------------------
 * This file is part of TICPY Framework.
 *
 * TICPY Framework is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License version 3
 * along with this program; if not,  see <http://www.gnu.org/licenses/>
 * or write to the Free Software Foundation, Inc., 51 Franklin Street,
 * Fifth Floor, Boston, MA  02110-1301, USA.
 *
----------------------------------------------------------------------------
 * Este archivo es parte del Framework TICPY.
 *
 * El TICPY Framework es software libre; Usted puede redistribuirlo y/o
 * modificarlo bajo los términos de la GNU Lesser General Public Licence versión 3
 * de la Free Software Foundation.
 *
 * Este programa es distribuido con la esperanza que sea de utilidad,
 * pero sin NINGUNA GARANTÍA; sin una garantía implícita de ADECUACION a cualquier
 * MERCADO o APLICACION EN PARTICULAR. vea la GNU General Public Licence
 * más detalles.
 *
 * Usted deberá haber recibido una copia de la GNU Lesser General Public Licence versión 3
 * "LICENCA.txt", junto con este programa; en caso que no, acceda a <http://www.gnu.org/licenses/>
 * o escriba a la Free Software Foundation (FSF) Inc.,
 * 51 Franklin St, Fifth Floor, Boston, MA 02111-1301, USA.
 */

package org.ticpy.tekoporu.junit;

import org.jboss.weld.environment.se.Weld;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

import org.ticpy.tekoporu.lifecycle.AfterShutdownProccess;
import org.ticpy.tekoporu.lifecycle.AfterStartupProccess;
import org.ticpy.tekoporu.util.Beans;

public class DemoiselleRunner extends BlockJUnit4ClassRunner {

	private Integer testsLeft;

	public DemoiselleRunner(Class<?> testClass) throws InitializationError {
		super(testClass);
		this.testsLeft = this.testCount();
	}

	@Override
	protected void runChild(FrameworkMethod method, RunNotifier notifier) {
		super.runChild(method, notifier);

		if (--this.testsLeft == 0) {
			this.shutdown();
		}
	}

	@Override
	public void run(RunNotifier notifier) {
		Weld weld = new Weld();
		weld.initialize();

		this.startup();
		super.run(notifier);

		weld.shutdown();
	}

	protected Object createTest() throws Exception {
		return Beans.getReference(getTestClass().getJavaClass());
	}

	private void startup() {
		Beans.getBeanManager().fireEvent(new AfterStartupProccess() {
		});
	}

	private void shutdown() {
		Beans.getBeanManager().fireEvent(new AfterShutdownProccess() {
		});
	}
}
