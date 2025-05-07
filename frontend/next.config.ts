import type { NextConfig } from "next";

// next.config.js
/** @type {import('next').NextConfig} */
const nextConfig = {
  typescript: {
    // Derleme sırasında TS hatalarını ignore et
    ignoreBuildErrors: true,
  },
  eslint: {
    // ESLint hatalarını da ignore etmek istersen
    ignoreDuringBuilds: true,
  },
  async rewrites() {
    return [
      {
        source: '/api/:path*',
        destination: 'https://yazilimmuh-e26607ab6869.herokuapp.com/api/:path*',
      },
    ]
  }
};

module.exports = nextConfig;