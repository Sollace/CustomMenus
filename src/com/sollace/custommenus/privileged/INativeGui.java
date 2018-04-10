package com.sollace.custommenus.privileged;

import java.io.IOException;

public interface INativeGui {
	void onMouseClick(int mouseX, int mouseY, int mouseButton) throws IOException;
}
