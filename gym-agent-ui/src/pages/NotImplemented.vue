<template>
  <div style="padding: 24px">
    <h2 style="margin: 0 0 12px 0">该页面尚未迁移</h2>
    <div style="color: #666">Route: {{ currentRoute }}</div>
    <div style="color: #666" v-if="Object.keys(currentQuery).length">Query: {{ currentQuery }}</div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const currentRoute = computed(() => route.path)
const currentQuery = computed(() => route.query)
</script>

