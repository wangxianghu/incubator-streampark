/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.streampark.e2e.pages.flink.applications;

import org.apache.streampark.e2e.pages.common.Constants;

import lombok.Getter;
import lombok.SneakyThrows;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

@Getter
public final class FlinkSQLYarnApplicationForm {

    private WebDriver driver;

    @FindBy(xpath = "//div[contains(@codefield, 'versionId')]//div[contains(@class, 'ant-select-selector')]")
    private WebElement buttonFlinkVersionDropdown;

    @FindBys({
            @FindBy(css = "[codefield=versionId]"),
            @FindBy(className = "ant-select-item-option-content")
    })
    private List<WebElement> selectFlinkVersion;

    public FlinkSQLYarnApplicationForm(WebDriver driver) {
        this.driver = driver;

        PageFactory.initElements(driver, this);
    }

    @SneakyThrows
    public FlinkSQLYarnApplicationForm add(String flinkVersion, String flinkSql) {
        buttonFlinkVersionDropdown.click();
        new WebDriverWait(driver, Constants.DEFAULT_WEBDRIVER_WAIT_DURATION)
            .until(ExpectedConditions.visibilityOfAllElements(selectFlinkVersion));
        selectFlinkVersion.stream()
            .filter(e -> e.getText().equalsIgnoreCase(flinkVersion))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Flink version not found"))
            .click();

        new FlinkSQLEditor(driver).content(flinkSql);

        return this;
    }
}
