import axios from 'axios';

const API_URL = '/api/topics';

/**
 * Fetch topics
 * @returns {Promise} Topics
 */
export function getTopics() {
    return axios.get(`${API_URL}`);
}

/**
 * Get terms for a topic
 * @param {number} topicId
 * @returns {Promise} Term
 */
export function getTermsForTopic(topicId) {
    return axios.get(`${API_URL}/${topicId}/terms`);
}
/**
 * Delete topic
 * @param {number} topicId
 * @returns {Promise} response
 */
export function deleteTopic(topicId) {
    return axios.delete(`${API_URL}/${topicId}/`);
}
/**
 * Create topic
 * @param {string} topic
 * @returns {Promise} Created topic
 */
export function createTopic(topic) {
    return axios.post(`${API_URL}/`, { name: topic });
}

/**
 * Edit topic
 * @param {string} topicId
 * @param {string} topic
 * @returns {Promise} Edited topic
 */
export function editTopic(topicId, topic) {
    return axios.patch(`${API_URL}/${topicId}`, { name: topic });
}
/**
 * Delete term
 * @param {number} topicId
 * @param {number} termId
 * @returns {Promise} response
 */
export function deleteTerm(topicId, termId) {
    return axios.delete(`${API_URL}/${topicId}/terms/${termId}`);
}
/**
 * Create term for a topic
 * @param {number} topicId
 * @param {string} term
 * @returns {Promise} Created term
 */
export function createTerm(topicId, term) {
    return axios.post(`${API_URL}/${topicId}/terms`, { topicId, name: term });
}

export function fetchTopic(id) {
    return axios.get(`${API_URL}/${id}`);
}
