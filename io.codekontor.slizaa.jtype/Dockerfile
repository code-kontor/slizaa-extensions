#
# io.codekontor.slizaa.jtype - Slizaa Static Software Analysis Tools
# Copyright © 2019 Code-Kontor GmbH and others (slizaa@codekontor.io)
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU Affero General Public License for more details.
#
# You should have received a copy of the GNU Affero General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.
#

FROM adoptopenjdk/openjdk11:x86_64-alpine-jdk-11.0.9_11.1

ARG SLIZAA_JTYPE_VERSION

ENV JAVA_APP_DIR /opt/slizaa
ENV JAVA_MAJOR_VERSION 11
ENV JAVA_APP_JAR slizaa-web-${SLIZAA_JTYPE_VERSION}.jar
ENV JAVA_APP_NAME SlizaaServer

USER root
RUN addgroup -S slizaa && adduser -S slizaa -G slizaa

ADD target/slizaa_${SLIZAA_JTYPE_VERSION}.tar.gz ${JAVA_APP_DIR}/
RUN mv "${JAVA_APP_DIR}"/slizaa_${SLIZAA_JTYPE_VERSION}/* "${JAVA_APP_DIR}" && \
  rmdir "${JAVA_APP_DIR}"/slizaa_${SLIZAA_JTYPE_VERSION}
ADD target/docker-extra/run-java/run-java.sh  ${JAVA_APP_DIR}/

RUN mkdir "${JAVA_APP_DIR}"/content

RUN chown -R slizaa:slizaa "${JAVA_APP_DIR}" && \
  chmod u+x "${JAVA_APP_DIR}"/run-java.sh

USER slizaa

EXPOSE 8085
WORKDIR ${JAVA_APP_DIR}
ENTRYPOINT ./run-java.sh
