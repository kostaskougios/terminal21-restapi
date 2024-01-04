package org.terminal21.client.components

import org.terminal21.client.components.UiElement.HasChildren

trait UiComponent extends UiElement with HasChildren[UiComponent]
