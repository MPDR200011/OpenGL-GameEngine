#version 400 core

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;
in float visibility;

out vec4 out_Colour;

uniform sampler2D textureSampler;
uniform vec3 lightColour;
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColor;

void main (void) {

    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitToLight = normalize(toLightVector);

    float nDot = dot(unitNormal,unitToLight);
    float brightness = max(nDot, 0.5);

    vec3 difuse = brightness * lightColour;

    vec3 unitToCameraVector = normalize(toCameraVector);
    vec3 lightDirection = -unitToLight;
    vec3 reflectedLightDir = reflect(lightDirection, unitNormal);

    float specularFactor = dot(reflectedLightDir,unitToCameraVector);
    specularFactor = max(specularFactor,0.0);
    float dampedFactor = pow(specularFactor, shineDamper);
    vec3 finalSpecular = dampedFactor * reflectivity * lightColour;

    out_Colour = (vec4(difuse, 0.0) * texture(textureSampler, pass_textureCoords) + vec4(finalSpecular,1.0));
    out_Colour = mix(vec4(skyColor, 1.0), out_Colour, visibility);
}
