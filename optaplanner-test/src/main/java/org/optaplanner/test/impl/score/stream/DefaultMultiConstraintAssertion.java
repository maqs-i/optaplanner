/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.optaplanner.test.impl.score.stream;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.Map;

import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.api.score.constraint.ConstraintMatchTotal;
import org.optaplanner.core.api.score.constraint.Indictment;
import org.optaplanner.core.api.score.stream.ConstraintProvider;
import org.optaplanner.core.impl.score.director.AbstractScoreDirector;
import org.optaplanner.test.api.score.stream.MultiConstraintAssertion;

public final class DefaultMultiConstraintAssertion<Solution_>
        implements MultiConstraintAssertion {

    private final ConstraintProvider constraintProvider;
    private final Score<?> actualScore;
    private final Collection<ConstraintMatchTotal> constraintMatchTotalCollection;
    private final Collection<Indictment> indictmentCollection;

    protected DefaultMultiConstraintAssertion(ConstraintProvider constraintProvider, Score<?> actualScore,
            Map<String, ConstraintMatchTotal> constraintMatchTotalMap, Map<Object, Indictment> indictmentMap) {
        this.constraintProvider = requireNonNull(constraintProvider);
        this.actualScore = requireNonNull(actualScore);
        this.constraintMatchTotalCollection = requireNonNull(constraintMatchTotalMap).values();
        this.indictmentCollection = requireNonNull(indictmentMap).values();
    }

    @Override
    public final void scores(Score<?> score, String message) {
        if (actualScore.equals(score)) {
            return;
        }
        Class<?> constraintProviderClass = constraintProvider.getClass();
        String expectation = message == null ? "Broken expectation." : message;
        throw new AssertionError(expectation + System.lineSeparator() +
                "  Constraint provider: " + constraintProviderClass + System.lineSeparator() +
                "       Expected score: " + score + " (" + score.getClass() + ")" + System.lineSeparator() +
                "         Actual score: " + actualScore + " (" + actualScore.getClass() + ")" +
                System.lineSeparator() + System.lineSeparator() +
                "  " + AbstractScoreDirector.explainScore(actualScore, constraintMatchTotalCollection, indictmentCollection));
    }

}
