#version 330 core
layout (location = 0) in vec2 vector;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main() {
    gl_Position = projection * view * model * vec4(vector, 0.0, 1.0);
}