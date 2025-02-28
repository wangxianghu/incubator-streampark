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
import org.apache.streampark.e2e.pages.flink.applications.entity.ApplicationsDynamicParams;

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
public final class ApplicationForm {

    private WebDriver driver;

    @FindBy(xpath = "//div[contains(@codefield, 'jobType')]//div[contains(@class, 'ant-select-selector')]")
    private WebElement buttonDevelopmentModeDropdown;

    @FindBys({
            @FindBy(css = "[codefield=jobType]"),
            @FindBy(className = "ant-select-item-option-content")
    })
    private List<WebElement> selectDevelopmentMode;

    @FindBy(xpath = "//div[contains(@codefield, 'executionMode')]//div[contains(@class, 'ant-select-selector')]")
    private WebElement buttonExecutionModeDropdown;

    @FindBys({
            @FindBy(css = "[codefield=executionMode]"),
            @FindBy(className = "ant-select-item-option-content")
    })
    private List<WebElement> selectExecutionMode;

    @FindBy(id = "form_item_jobName")
    private WebElement inputApplicationName;

    @FindBy(xpath = "//button[contains(@class, 'ant-btn')]//span[contains(text(), 'Submit')]")
    private WebElement buttonSubmit;

    @FindBy(xpath = "//button[contains(@class, 'ant-btn')]//span[contains(text(), 'Cancel')]")
    private WebElement buttonCancel;

    public ApplicationForm(WebDriver driver) {
        this.driver = driver;

        PageFactory.initElements(driver, this);
    }

    @SneakyThrows
    public ApplicationForm addApplication(
                                          DevelopmentMode developmentMode,
                                          ExecutionMode executionMode,
                                          String applicationName,
                                          String flinkVersion,
                                          ApplicationsDynamicParams applicationsDynamicParams) {
        Thread.sleep(Constants.DEFAULT_SLEEP_MILLISECONDS);
        new WebDriverWait(driver, Constants.DEFAULT_WEBDRIVER_WAIT_DURATION)
            .until(ExpectedConditions.elementToBeClickable(buttonDevelopmentModeDropdown));
        buttonDevelopmentModeDropdown.click();
        new WebDriverWait(driver, Constants.DEFAULT_WEBDRIVER_WAIT_DURATION)
            .until(ExpectedConditions.visibilityOfAllElements(selectDevelopmentMode));
        switch (developmentMode) {
            case CUSTOM_CODE:
                selectDevelopmentMode.stream()
                    .filter(e -> e.getText().equalsIgnoreCase(DevelopmentMode.CUSTOM_CODE.desc()))
                    .findFirst()
                    .orElseThrow(
                        () -> new IllegalArgumentException(
                            String.format("Development mode not found: %s",
                                developmentMode.desc())))
                    .click();
                break;
            case FLINK_SQL:
                selectDevelopmentMode.stream()
                    .filter(e -> e.getText().equalsIgnoreCase(DevelopmentMode.FLINK_SQL.desc()))
                    .findFirst()
                    .orElseThrow(
                        () -> new IllegalArgumentException(
                            String.format("Development mode not found: %s",
                                developmentMode.desc())))
                    .click();
                buttonExecutionModeDropdown.click();
                switch (executionMode) {
                    case REMOTE:
                        selectExecutionMode.stream()
                            .filter(e -> e.getText().equalsIgnoreCase(ExecutionMode.REMOTE.desc()))
                            .findFirst()
                            .orElseThrow(
                                () -> new IllegalArgumentException(
                                    String.format("Execution mode not found: %s",
                                        executionMode.desc())))
                            .click();
                        break;
                    case YARN_APPLICATION:
                        selectExecutionMode.stream()
                            .filter(e -> e.getText()
                                .equalsIgnoreCase(ExecutionMode.YARN_APPLICATION.desc()))
                            .findFirst()
                            .orElseThrow(
                                () -> new IllegalArgumentException(
                                    String.format("Execution mode not found: %s",
                                        executionMode.desc())))
                            .click();
                        new FlinkSQLYarnApplicationForm(driver)
                            .add(flinkVersion, applicationsDynamicParams.flinkSQL());
                        break;
                    case YARN_SESSION:
                        selectExecutionMode.stream()
                            .filter(e -> e.getText().equalsIgnoreCase(ExecutionMode.YARN_SESSION.desc()))
                            .findFirst()
                            .orElseThrow(
                                () -> new IllegalArgumentException(
                                    String.format("Execution mode not found: %s",
                                        executionMode.desc())))
                            .click();
                        break;
                    case KUBERNETES_SESSION:
                        selectExecutionMode.stream()
                            .filter(e -> e.getText()
                                .equalsIgnoreCase(ExecutionMode.KUBERNETES_SESSION.desc()))
                            .findFirst()
                            .orElseThrow(
                                () -> new IllegalArgumentException(
                                    String.format("Execution mode not found: %s",
                                        executionMode.desc())))
                            .click();
                        break;
                    case KUBERNETES_APPLICATION:
                        selectExecutionMode.stream()
                            .filter(
                                e -> e.getText().equalsIgnoreCase(
                                    ExecutionMode.KUBERNETES_APPLICATION.desc()))
                            .findFirst()
                            .orElseThrow(
                                () -> new IllegalArgumentException(
                                    String.format("Execution mode not found: %s",
                                        executionMode.desc())))
                            .click();
                        break;
                    case YARN_PER_JOB:
                        selectExecutionMode.stream()
                            .filter(e -> e.getText().equalsIgnoreCase(ExecutionMode.YARN_PER_JOB.desc()))
                            .findFirst()
                            .orElseThrow(
                                () -> new IllegalArgumentException(
                                    String.format("Execution mode not found: %s",
                                        executionMode.desc())))
                            .click();
                        new FlinkSQLYarnApplicationForm(driver)
                            .add(flinkVersion, applicationsDynamicParams.flinkSQL());
                        break;
                    default:
                        throw new IllegalArgumentException(
                            String.format("Unknown execution mode: %s", executionMode.desc()));
                }
                break;
            case PYTHON_FLINK:
                selectDevelopmentMode.stream()
                    .filter(e -> e.getText().equalsIgnoreCase(DevelopmentMode.PYTHON_FLINK.toString()))
                    .findFirst()
                    .orElseThrow(
                        () -> new IllegalArgumentException(
                            String.format("Development mode not found: %s",
                                developmentMode)))
                    .click();
                break;
            default:
                throw new IllegalArgumentException(
                    String.format("Unknown development mode: %s", developmentMode));
        }
        inputApplicationName.sendKeys(applicationName);

        buttonSubmit.click();

        return this;
    }

    @Getter
    public enum DevelopmentMode {

        CUSTOM_CODE("custom code"), FLINK_SQL("flink sql"), PYTHON_FLINK("python flink"),
        ;

        private final String desc;

        DevelopmentMode(String desc) {
            this.desc = desc;
        }
    }

    @Getter
    public enum ExecutionMode {

        REMOTE("remote"), YARN_APPLICATION("yarn application"), YARN_SESSION("yarn session"), KUBERNETES_SESSION(
            "kubernetes session"),
        KUBERNETES_APPLICATION("kubernetes application"), YARN_PER_JOB(
            "yarn per-job (deprecated, please use yarn-application mode)"),
            ;

        private final String desc;

        ExecutionMode(String desc) {
            this.desc = desc;
        }
    }
}
